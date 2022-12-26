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

const rider = sequelize.define("rider", {
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
	},
    vehicle: {
        type: DataTypes.STRING,
		allowNull: false,
    },
	token: {
		type: DataTypes.STRING,
	},
    photo: {
        type: DataTypes.BLOB
    },
});

module.exports = sequelize.model("rider", rider)