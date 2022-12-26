require("dotenv").config();
require("./configs/database").connect();
const express = require("express");
const app = express();
const port = process.env.PORT || 3000;

const bodyParser = require("body-parser");
const cors = require('cors')

app.use(cors())
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: true }));


app.use("/api/client", require("./routes/client.routes"));
app.use("/api/rider", require("./routes/rider.routes"));
app.use("/api/restaurant", require("./routes/restaurant.routes"));
app.use("/api/order", require("./routes/order.routes"));

app.listen(port, () => {
	console.log(`Example app listening on port ${port}`);
});

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

module.exports = { sequelize };


