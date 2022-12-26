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

const restaurant = sequelize.define("restaurant", {
	name: {
		type: DataTypes.STRING,
		allowNull: false,
	},
	address: {
		type: DataTypes.STRING,
		allowNull: false,
	},
    photo: {
        type: DataTypes.BLOB
    },
	description: {
		type: DataTypes.STRING,
	},
});

module.exports = sequelize.model("restaurant", restaurant)