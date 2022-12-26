const express = require("express");
const router = express.Router();

const auth = require("../middleware/auth");
const clientController = require("../controllers/client.controller.js");

const multer = require("multer");
var storage = multer.diskStorage({
	destination: function (req, file, cb) {
	  console.log("...")
	  console.log(req.body)
	  console.log(file)
	  console.log(req.body.internalUserID) // YAY, IT'S POPULATED
	  cb(null, 'uploads/')
	},                    
	filename: function (req, file, cb) {
		console.log("---")
		console.log(file)
		console.log(req.body["filename"])
	  cb(null, req.body["filename"])
	}                     
  }); 
const upload = multer({storage: storage})

router.get("/", auth, (req, res) => {
	res.send("hello world");
});

// Register
router.post("/register", upload.single("photo"), clientController.register);

// Login
router.post("/login", clientController.login);

router.post("/upload/:id", upload.single("photo"), clientController.upload);

//router.get("/download/:filename", clientController.getImg);

router.get("/photo/:id", clientController.download);

module.exports = router;
