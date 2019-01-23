const jwt = require("jsonwebtoken");

exports.register = (req, res) => {
    const {username, password} = req.body;
    res.json({
        username,
        password
    });
};