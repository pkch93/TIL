const passport = require("passport");
const localStrategy = require("passport-local").Strategy;
const jwt = require("jsonwebtoken");

const users = require("../../../model/dummy/user.json")["users"];

passport.use("local", new localStrategy((username, password, done) => {
    for(let user in users){
        if (user.username === username){
            if (user.password !== password){
                return done(null, false, { message: "incorrect password" });
            }
            return done(null, user);
        }
    }
    return done(null, false, { message: "incorrect username" });
}));

exports.login = (req, res, next) => {
    if (passport.authenticate("local")){
        
        next();
        return true;
    }
    res.status(403).json({ error: "403 Authentication Denied", message: "invaild input" });
};

exports.register = (req, res) => {
    const {username, password} = req.body;
    res.json({
        username,
        password
    });
};