const Client = require("../models/client.model");

const bcrypt = require("bcryptjs");
const jwt = require("jsonwebtoken");

const { dirname } = require("path");
const appDir = dirname(require.main.filename);

exports.register = async (req, res) => {
	try {
		// Get client input
		const { name, address, email, password } = req.body;

		const photo = req.file;

		// Validate client input
		if (!(email && password && name && address)) {
			res.status(400).send("All input is required");
		}

		// check if client already exist
		// Validate if client exist in our database
		const oldClient = await Client.findOne({ where: { email: email } });
		if (oldClient) {
			return res.status(409).send("User Already Exist. Please Login");
		}

		//Encrypt user password
		encryptedPassword = await bcrypt.hash(password, 10);

		// Create user in our database
		const client = await Client.create({
			name,
			//birthdate: new Date(Date.UTC(2016, 0, 1)),
			address,
			email: email.toLowerCase(), // sanitize: convert email to lowercase
			password: encryptedPassword,
			photo: photo ?? null,
		});

		// Create token
		const token = jwt.sign(
			{ user_id: client._id, email },
			process.env.TOKEN_KEY
		);
		// save client token
		client.token = token;
		await client.save();

		// return new client
		res.status(201).json(client);
	} catch (err) {
		console.log(err);
	}
	// Our register logic ends here
};

exports.login = async (req, res) => {
	// Our login logic starts here
	try {
		// Get client input
		const { email, password } = req.body;

		console.log(req.body)

		// Validate client input
		if (!(email && password)) {
			return res.status(400).send("All input is required");
		}
		// Validate if client exist in our database
		const client = await Client.findOne({
			where: {
				email: email,
			},
		});

		if (client && (await bcrypt.compare(password, client.password))) {
			// Create token
			const token = jwt.sign(
				{ user_id: client._id, email },
				process.env.TOKEN_KEY,
				{
					expiresIn: "2h",
				}
			);

			// save user token
			client.token = token;
			await client.save();

			// client
			console.log(client)
			res.status(200).json(client);
		} else {
			console.log("400 - Invalid Credentials")
			res.status(400).send("Invalid Credentials");
		}
	} catch (err) {
		console.log(err);
	}
	// Our register logic ends here
};

exports.upload = async (req, res) => {
	console.log("UPLOAD PHOTO")
	console.log(req.params);
	try {

		console.log(req.body)
		const id = req.params.id;
		console.log(id)

		const client = await Client.findOne({
			where: {
				id: id,
			},
		});

		console.log("found client")

		client.photo = req.body["filename"];
		await client.save();

		console.log(client);
		console.log(client.token);

		res.status(200).send(client);
	} catch (error) {
		console.log(error);
		res.status(400).send("Error while uploading file. Try again later.");
	}
};

exports.getImg = async (req, res) => {
	const filename = req.params["filename"];
	res.sendFile(appDir + "/uploads/" + filename);
};

exports.download = async (req, res) => {
	console.log(req.params);

	const client = await Client.findOne({
		where: {
			id: req.params.id,
		},
	});

	if (client.photo != null)
		res.status(200).sendFile(appDir + "/uploads/" + client.photo);
	else res.status(404).send("Foto não encontrada");
};

/* sequelize
	.authenticate()
	.then(() => {
		console.log("Connection has been established successfully.");
	})
	.catch((error) => {
		console.error("Unable to connect to the database: ", error);
	});

const Client = sequelize.define("client", {
	name: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	birthdate: {
		type: DataTypes.DATE,
		allowNull: false,
	},
	address: {
		type: DataTypes.STRING,
		allowNull: false,
	},
});

sequelize
	.sync()
	.then(() => {
		console.log("Client table created successfully!");

		Client.create({
			name: "cunha",
			birthdate: new Date(Date.UTC(2016, 0, 1)),
			address: "Tv xpto",
		})
			.then((res) => {
				console.log(res);
			})
			.catch((error) => {
				console.error("Failed to create a new record : ", error);
			});
	})
	.catch((error) => {
		console.error("Unable to create table : ", error);
	});


sequelize.sync().then(() => {

    Client.findAll().then(res => {
        console.log(res)
    }).catch((error) => {
        console.error('Failed to retrieve data : ', error);
    });

}).catch((error) => {
    console.error('Unable to create table : ', error);
}); */
