const express = require("express");
const router = express.Router();

const auth = require("../middleware/auth");
const orderController = require("../controllers/order.controller.js");

router.get("/", auth, (req, res) => {
	res.send("hello world");
});

// Create order
router.post("/create", orderController.create);

// Rider accept order
router.put("/accept", orderController.accept);

// Fetch active orders for client
router.get("/client/:id/active", orderController.getClientActiveOrders);

// Fetch orders for rider
router.get("/rider/:id", orderController.getRiderOrders);

// Confirm order delivery
router.get("/delivery/:id", orderController.deliverOrder)

// Update rider coords
router.put("/rider/update", orderController.updateRiderCoords)

// Get rider coords
router.get("/:id/coords", orderController.getRiderCoords)

module.exports = router;
