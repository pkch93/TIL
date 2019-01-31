module.exports = (sequelize, DataTypes) => {
    return sequelize.define('comment', {
        title: {
            type: DataTypes.STRING(100),
            allowNull: false
        },
        content: {
            type: DataTypes.STRING,
            allowNull: false
        },
        createdAt: {
            type: DataTypes.DATE,
            defaultValue: DataTypes.NOW
        }
    }, {
        timestamps: false
    });
}