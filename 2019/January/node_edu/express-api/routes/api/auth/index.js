const router = require("express").Router();
const authController = require("./auth.controller");

router.post("/register", authController.register);

module.exports = router;