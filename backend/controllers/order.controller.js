const { Sequelize, DataTypes } = require("sequelize");

const Order = require("../models/order.model");
const Rider = require("../models/rider.model");

exports.create = async (req, res) => {
	try {
		// Get order input
		const {
			restaurant_name,
			restaurant_address,
			client_id,
			client_name,
			client_address,
		} = req.body;

		console.log(req.body);

		// Validate order input
		if (
			!(
				restaurant_name &&
				restaurant_address &&
				client_id &&
				client_name &&
				client_address
			)
		) {
			res.status(400).send("All input is required");
		}

		const order_status = "Waiting";
		// Create order in our database
		const order = await Order.create({
			restaurant_name,
			restaurant_address,
			client_id,
			client_name,
			client_address,
			order_status,
		});

		// return new order
		res.status(201).json(order);
	} catch (err) {
		console.log(err);
	}
};

exports.accept = async (req, res) => {
	try {
		console.log("accept")
		const { order_id, rider_name, rider_lat, rider_lng, order_status } =
			req.body;

		console.log(req.body)

		console.log(order_id, rider_name, rider_lat, rider_lng, order_status)
		console.log(!(order_id && rider_name && rider_lat && rider_lng && order_status))
		// Validate order input
		if (
			!(order_id && rider_name && rider_lat && rider_lng && order_status)
		) {
			console.log("All input is required")
			return res.status(400).send("All input is required");
		}

		// Validate if order exist in our database
		const order = await Order.findOne({
			where: {
				id: order_id,
			},
		});
		console.log(order)

		if (order == null) {
			return res.status(400).send("Order doesnt exist");
		}

		if (order.order_status != "Delivering") {
			// Create order in our database
			order.rider_name = rider_name;
			order.rider_lat = rider_lat;
			order.rider_lng = rider_lng;
			order.order_status = order_status;
			const updatedOrder = await order.save();

			// return new order
			return res.status(201).json(updatedOrder);
		} else {
			return res.status(400).send("Order already being delivered");
		}
	} catch (err) {
		console.log(err);
	}
};

exports.getClientActiveOrders = async (req, res) => {
	const clientId = req.params.id;

	if (!clientId) return res.status(400).send("Client id is required");

	const orders = await Order.findAll({
		where: Sequelize.and(
			{ client_id: clientId },
			Sequelize.or(
				{ order_status: "Delivering" },
				{ order_status: "Waiting" }
			)
		),
	});

	return res.status(200).json(orders);
};

exports.getRiderOrders = async (req, res) => {
	const riderId = req.params.id;

	if (!riderId) return res.status(400).send("Rider id is required");

	let rider = await Rider.findOne({
		where: { id: riderId },
	});

	if (!rider) return res.status(400).send("Rider id is invalid");

	let orders = await Order.findAll({
		where: {
			rider_name: rider.name,
			order_status: "Delivering",
		},
	});

	if (orders.length == 0)
		orders = await Order.findAll({
			where: Sequelize.and(
				{ rider_name: null },
				Sequelize.or(
					{ order_status: "Delivering" },
					{ order_status: "Waiting" }
				)
			),
		});

	return res.status(200).json(orders);
};

exports.deliverOrder = async (req, res) => {
	const orderId = req.params.id;

	if (!orderId) return res.status(400).send("Order id is required");

	const order = await Order.findOne({
		where: {
			id: orderId,
		},
	});

	if (!order) return res.status(404).json({error: true, message:"Order not found"});

	order.order_status = "Delivered";
	const newOrder = await order.save();

	return res.status(200).json(newOrder);
};

exports.updateRiderCoords = async (req, res) => {
	const { order_id, rider_lat, rider_lng } = req.body;

	if (!(order_id && rider_lat && rider_lng)) {
		res.status(400).send("All input is required");
	}

	const order = await Order.findOne({
		where: {
			id: order_id,
		},
	});

	order.rider_lat = rider_lat;
	order.rider_lng = rider_lng;
	const updatedOrder = await order.save();

	return res.status(200).json(updatedOrder);
};

exports.getRiderCoords = async (req, res) => {
	const order_id = req.params.id;

	if (!(order_id)) {
		res.status(400).send("Order id is required");
	}

	const order = await Order.findOne({
		where: {
			id: order_id,
		},
	});

	return res.status(200).json(order);
};
