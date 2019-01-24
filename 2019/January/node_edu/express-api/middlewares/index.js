const jwt = require("jsonwebtoken");

exports.isLogined = (req, res, next) => {
    if (req.isAuthenticated()) {
        next();
    } else {
        res.status(403).json({
            code: "403 Authentication Denied",
            message: "you have to login"}
        );
    }
};

exports.verifyToken = (req, res, next) => {
    try {
        req.decoded = jwt.verify(req.headers.authorization, process.env.JWT_SECRET);
        return next();
    } catch (err) {
        if (err.name === "TokenExpiredError") {
            return res.status(419).json({
                code: 419,
                message: "your jwt token is expired. please try to get jwt token again"
            });
        }
        return res.status(401).json({
            code: 401,
            message: "this token is invaild"
        });
    }
};
