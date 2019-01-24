require("dotenv").config();
const httpErrors = require("http-errors");
const express = require("express");
const logger = require("morgan");
const session = require("express-session");

const app = express();
const passport = require("passport");
const passportConfig = require("./config/passport");
passportConfig(passport);

app.set("dummy", __dirname + "/model/dummy");
const mode = process.env.NODE_MODE || "development";
const port = process.env.PORT || 3000;

app.use(logger("dev"));
app.use(express.urlencoded({extended: false}));
app.use(express.json());
app.use(session({
    resave: false,
    saveUninitialized: false,
    secret: process.env.COOKIE_SECRET,
    cookie: {
        httpOnly: true,
        secure: false
    }
}));
app.use(passport.initialize());
app.use(passport.session());
app.disable("etag");

const apiRoutes = require("./routes/api");
app.use("/api", apiRoutes);

app.get("/", (req, res) => {
    res.json({
        "message": "hello world!" 
    });
});

app.use((req, res, next) => {
    next(httpErrors(404));
});

app.use((err, req, res) => {
    // set locals, only providing error in development
    res.locals.message = err.message;
    res.locals.error = mode === "development" ? err : {};
  
    // render the error page
    res.status(err.status || 500);
    res.render("error");
});

app.listen(port, () => {
    console.log("server on!");
});