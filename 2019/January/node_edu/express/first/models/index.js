const path = require('path');
const Sequelize = require('sequelize');

const db = {};

const sequelize = new Sequelize('TEST', 'root', '1234', {
    host: 'localhost',
    port: '3306',
    dialect: 'mysql',
});

db.sequelize = sequelize;
db.Sequelize = Sequelize;

// Schema define
db.User = require('./user')(sequelize, Sequelize);
db.Post = require('./post')(sequelize, Sequelize);
db.Comment = require('./comment')(sequelize, Sequelize);

// Relation define
db.User.hasMany(db.Post, { foreignKey: 'fk_user_post', sourceKey: 'id' });
db.Post.belongsTo(db.User, { foreignKey: 'fk_user_post', targetKey: 'id' });
db.User.hasMany(db.Comment, { foreignKey: 'fk_user_comment', sourceKey: 'id' });
db.Comment.belongsTo(db.User, { foreignKey: 'fk_user_comment', sourceKey: 'id' });
db.Post.hasMany(db.Comment, { foreignKey: 'fk_post_comment', sourceKey: 'id' });
db.Comment.belongsTo(db.User, { foreignKey: 'fk_post_comment', sourceKey: 'id' });

module.exports = db;