const express = require("express");
const router = express.Router();

const auth = require("../middleware/auth");
const riderController = require("../controllers/rider.controller.js");

const multer = require("multer");
var storage = multer.diskStorage({
	destination: function (req, file, cb) {
	  console.log(req.body.internalUserID) // YAY, IT'S POPULATED
	  cb(null, 'uploads/')
	},                    
	filename: function (req, file, cb) {
	  cb(null, req.body["filename"])
	}                     
  }); 
const upload = multer({storage: storage})

// Register
router.post("/register", upload.single("photo"), riderController.register);

// Login
router.post("/login", riderController.login);

router.post("/upload", upload.single("photo"), riderController.upload);

router.get("/photo/:id", riderController.download);

module.exports = router;
