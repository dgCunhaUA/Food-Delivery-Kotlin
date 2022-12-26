const Rider = require("../models/rider.model");

const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

const { dirname } = require("path");
const appDir = dirname(require.main.filename);

exports.register = async (req, res) => {
	try {
		// Get rider input
		const { name, address, email, password, vehicle } =
			req.body;
			
		const photo = req.file;

		// Validate rider input
		if (
			!(
				email &&
				password &&
				name &&
				address &&
                vehicle
			)
		) {
			res.status(400).send("All input is required");
		}

		// check if rider already exist
		// Validate if rider exist in our database
		const oldRider = await Rider.findOne({ where: { email: email } });
		if (oldRider) {
			return res.status(409).send("Rider Already Exist.");
		}

		//Encrypt user password
		encryptedPassword = await bcrypt.hash(password, 10);

		// Create user in our database
		const rider = await Rider.create({
			name,
			//birthdate: new Date(Date.UTC(2016, 0, 1)),
			address,
			email: email.toLowerCase(), // sanitize: convert email to lowercase
			password: encryptedPassword,
			vehicle: vehicle,
			photo: photo ?? null,
		});

		// Create token
		const token = jwt.sign(
			{ user_id: rider._id, email },
			process.env.TOKEN_KEY,
		);
		// save rider token
		rider.token = token;
		await rider.save();

		// return new rider
		res.status(201).json(rider);
	} catch (err) {
		console.log(err);
	}
	// Our register logic ends here
};

exports.login = async (req, res) => {
	// Our login logic starts here
	try {
		// Get rider input
		const { email, password } = req.body;

		// Validate rider input
		if (!(email && password)) {
			res.status(400).send("All input is required");
		}
		// Validate if rider exist in our database
		const rider = await Rider.findOne({
			where: {
				email: email,
			},
		});

		if (rider && (await bcrypt.compare(password, rider.password))) {
			// Create token
			const token = jwt.sign(
				{ rider_id: rider._id, email },
				process.env.TOKEN_KEY,
			);

			// save user token
			rider.token = token;
			await rider.save();

			// rider
			res.status(200).json(rider);
		} else {
			res.status(400).send("Invalid Credentials");
		}
	} catch (err) {
		console.log(err);
	}
	// Our register logic ends here
};



exports.upload = async (req, res) => {

	try {
		const id = req.body["id"];

		const rider = await Rider.findOne({
			where: {
				id: id,
			},
		});

		rider.photo = req.body["filename"];
		await rider.save();

		console.log(rider);

		res.status(200).send(rider);
	} catch (error) {
		console.log(error)
		res.status(400).send("Error while uploading file. Try again later.");
	}
};

exports.getImg = async (req, res) => {
	const filename = req.params["filename"]
	res.sendFile(appDir+"/uploads/"+filename)
}

exports.download = async (req, res) => {

	const rider = await Rider.findOne({
		where: {
			id: req.params.id,
		},
	});

	if( rider.photo != null)
		res.status(200).sendFile(appDir+"/uploads/"+rider.photo)
	else
		res.status(404).send("Foto não encontrada")
}