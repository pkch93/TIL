const passport = require("passport");



exports.login = (req, res) => {

    res.status(403).json({ error: "403 Authentication Denied", message: "invaild input" });
};

exports.register = (req, res) => {
    const {username, password} = req.body;
    res.json({
        username,
        password
    });
};