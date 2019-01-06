const path = require('path');
const Sequelize = require('sequelize');

const db = {};

const sequelize = new Sequelize('mysql', 'pkch', '1234', {
    host: 'localhost',
    dialect: 'mysql',
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

db.User = require('./user')(sequelize, Sequelize);

module.exports = db;