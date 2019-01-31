module.exports = (sequelize, DataTypes) => {
    return sequelize.define('user', {
        name: {
            type: DataTypes.STRING(20),
            allowNull: false,
            unique: true
        },
        password: {
            type: DataTypes.STRING(20),
            allowNull: false,
        },
        age: {
            type: DataTypes.INTEGER.UNSIGNED,
            allowNull: false
        },
        created_at: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        }
    }, {
        timestamps: false
    });
};