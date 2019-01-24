const passport = require("passport");
const JwtStrategy = require("passport-jwt").Strategy,
    ExtractJwt = require("passport-jwt").ExtractJwt;

const jwtSecret = process.env.JWT_SECRET;

const users = require("../../../model/dummy/user.json")["users"];
const opts = {};

opts.jwtFromRequest = ExtractJwt.fromAuthHeaderAsBearerToken();
opts.secretOrKey = jwtSecret;
opts.issuer = "pkch9377@gmail.com";
opts.audience = "everybody";

passport.use("jwt", new JwtStrategy(opts, (jwtPayload, done) => {
    const {username, password} = jwtPayload;
    for(let user in users){
        if (user.username === username && user.password === password){
            return done(null, user);
        }
    }
    return done(null, false);
}));

exports.isLogined = (req, res, next) => {
    if (passport.authenticate("jwt")){
        next();
    }
    res.status(401).json({
        error: 401,
        message: "401 unAuthorization"
    });
};
