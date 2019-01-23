const passport = require("passport");
const localStrategy = require("passport-local").Strategy;
const JwtStrategy = require("passport-jwt").Strategy,
    ExtractJwt = require("passport-jwt").ExtractJwt;

const jwt = require("jsonwebtoken");
const jwtSecret = process.env.JWT_SECRET;

const users = require("../../../model/dummy/user.json")["users"];
const opts = {};

opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
opts.secretOrKey = jwtSecret;
opts.issuer = "pkch9377@gmail.com";
opts.audience = "everybody";

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

passport.use("jwt", new JwtStrategy(opts, (jwtPayload, done) => {
    const {username, password} = jwtPayload;
    for(let user in users){
        if (user.username === username && user.password === password){
            return done(null, user);
        }
    }
    return done(null, false);
}));

exports.login = (req, res) => {
    const {username, password} = req.body;
    for(let user of users){
        if (user.username === username && user.password === password){
            res.status(200).json(user);
            return true;
        }
    }
    // res.writeHead(403);
    res.status(403).json({ error: "403 Authentication Denied", message: "invaild input" });
};

exports.register = (req, res) => {
    const {username, password} = req.body;
    res.json({
        username,
        password
    });
};