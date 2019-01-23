const router = require("express").Router();
const userController = require("./user.controller");

router.get("/userlist", userController.getUserList);

module.exports = router;