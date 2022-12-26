//const sequelize = require('../server.js');
//const DataTypes = require('sequelize');
const { Sequelize, DataTypes } = require("sequelize");
const sequelize = new Sequelize(
    'proj1',
    'root',
    'root',
     {
       host: 'localhost',
       dialect: 'mysql'
     }
   );

const client = sequelize.define("client", {
	name: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	address: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	email: {
		type: DataTypes.STRING,
		allowNull: false,
		unique: true
	},
	password: {
		type: DataTypes.STRING,
		allowNull: false,
		unique: true
	},
	token: {
		type: DataTypes.STRING,
	},
	photo: {
        type: DataTypes.STRING
    },
});

module.exports = sequelize.model("client", client)