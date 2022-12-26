const { Sequelize, DataTypes } = require("sequelize");

const sequelize = new Sequelize("proj1", "root", "root", {
	host: "localhost",
	dialect: "mysql",
});

sequelize
	.authenticate()
	.then(() => {
		console.log("Connection has been established successfully.");
	})
	.catch((error) => {
		console.error("Unable to connect to the database: ", error);
	});

const order = sequelize.define("order", {
	restaurant_name: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	restaurant_address: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	client_id: {
		type: DataTypes.INTEGER,
		allowNull: false,
	},
	client_name: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	client_address: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	rider_name: {
		type: DataTypes.STRING,
	},
	rider_lat: {
		type: DataTypes.FLOAT,
	},
	rider_lng: {
		type: DataTypes.FLOAT,
	},
	order_status: {
		type: DataTypes.STRING,
	},
});

sequelize
	.sync()
	.then(() => {
		console.log("Client table created successfully!");
	})
	.catch((error) => {
		console.error("Unable to create table : ", error);
	});
