const express = require('express');
const router = express.Router();

const User = require('../models').User;

router.route('/').
  get((req, res) => {
    res.render('login', {title: "login"});
  })
  .post('/', (req, res, next) => {
    const {email, password} = req.body;
    User.findAll({
      where: {
        email,
        password
      }
    }).then( user => {
      res.render('index', { user })
    }).catch( err => {
      console.log(err);
      next(err);
    });
  });

module.exports = router;
