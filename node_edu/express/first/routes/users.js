const express = require('express');
const router = express.Router();

const User = require('../models').User;

router.route('/').
  get((req, res) => {
    res.render('login', {title: "login"});
  })
  .post((req, res, next) => {
    const {name, password} = req.body;
    User.findAll({
      where: {
        name,
        password
      }
    }).then( user => {
      console.log(user);
      res.render('index', { user: user.dataValues, title: 'Express practice' });
    }).catch( err => {
      console.log(err);
      next(err);
    });
  });

router.route('/join')
  .get( (req, res) => {
    res.render('join', { title: '회원가입' });
  })
  .post( (req, res, next) => {
    const { name, password, age } = req.body; 
    User.create({
      name,
      password,
      age
    }).then((data) => {
      res.redirect(201, '/');
    }).catch((err) => {
      console.log(err)
      next();
    });
  });

module.exports = router;
