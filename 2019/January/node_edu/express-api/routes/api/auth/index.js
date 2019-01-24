const router = require("express").Router();
const authController = require("./auth.controller");

router.post("/register", authController.register);
router.post("/login", authController.login, (req, res) => {
    res.status(200).json(req.user);
});


module.exports = router;