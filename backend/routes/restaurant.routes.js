const express = require("express");
const router = express.Router();
const auth = require("../middleware/auth");
const restaurantController = require("../controllers/restaurant.controller.js");

const multer = require('multer');
const upload = multer();


// get all restaurants
router.get("/", restaurantController.getAll);

// Add a new restaurant
router.post("/add", upload.single("photo"), restaurantController.add);



module.exports = router;
