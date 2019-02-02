const jwt = require("jsonwebtoken");

const users = require("../../model/dummy/user.json")["users"];

const findUser = ({username, password}) => {
    for (let user of users){
        if (user.username === username && user.password === password){
            return user;
        }
    }
    return null;
};

exports.login = (req, res) => {
    const {username, password} = req.body;
    const user = findUser({username, password});
    if (!user){
        res.status(403).json({ error: "403 Authentication Denied", message: "invaild input" });
    } else {
        const token = jwt.sign({
            username,
            password
        }, process.env.JWT_SECRET, {
            expiresIn: "24h",
            issuer: "test"
        });
        res.status(200)
            .set("Authorization", `Bearer ${token}`)
            .json({
                code: 200,
                message: "login successed",
                token
            });
    }
};

exports.register = (req, res) => {
    const {username, password} = req.body;
    res.json({
        username,
        password
    });
};