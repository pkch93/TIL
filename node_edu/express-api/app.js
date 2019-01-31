require("dotenv").config();
const express = require("express");
const logger = require("morgan");

const app = express();
const passport = require("passport");
const passportConfig = require("./config/passport");

app.set("dummy", __dirname + "/model/dummy");
const mode = process.env.NODE_MODE || "development";
const port = process.env.PORT || 3000;

app.use(logger("dev"));
app.use(express.urlencoded({extended: false}));
app.use(express.json());
app.use(passport.initialize());
passportConfig(passport);
app.disable("etag");

const apiRoutes = require("./routes/api");
const authRoutes = require("./routes/auth");
const { verifyToken } = require("./middlewares");

app.use("/api", verifyToken, apiRoutes);
app.use("/auth", authRoutes);

app.get("/", (req, res) => {
    res.json({
        "message": "hello world!" 
    });
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
