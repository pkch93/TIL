exports.getUserList = (req, res) => {
    const dummyurl = req.app.get("dummy");
    const userList = require(dummyurl + "/user.json");
    res.json(userList);  
};