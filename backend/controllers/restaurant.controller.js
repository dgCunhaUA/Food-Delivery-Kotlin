const Restaurant = require("../models/restaurant.model");


exports.add = async (req, res) => {

    try {
		// Get restaurant input
		const { name, address, description } =
			req.body;

        const photo = req.file
        
		// Validate restaurant input
		if (
			!(
				name &&
				address &&
				photo &&
				description
			)
		) {
			res.status(400).send("All input is required");
		}

		// check if restaurant already exist
		// Validate if restaurant exist in our database
		const oldRestaurant = await Restaurant.findOne({ where: { name: name } });
		if (oldRestaurant) {
			return res.status(409).send("Restaurant Already Exist.");
		}

		// Create restaurant in our database
		const restaurant = await Restaurant.create({
			name,
            address,
            photo,
            description,
		});

		// return new restaurant
		res.status(201).json(restaurant);
	} catch (err) {
		console.log(err);
	}

}

exports.getAll = async (req, res) => {

    try {
		// check if restaurant already exist
		// Validate if restaurant exist in our database
		const allRestaurants = await Restaurant.findAll();

		// return new restaurant
		res.status(201).json(allRestaurants);
	} catch (err) {
		console.log(err);
	}

}