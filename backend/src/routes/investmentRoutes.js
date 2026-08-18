const express = require('express');
const {
  getInvestments,
  createInvestment,
  getInvestmentById
} = require('../controllers/investmentController');
const { protect } = require('../middleware/auth');

const router = express.Router();

router.route('/')
  .get(protect, getInvestments)
  .post(protect, createInvestment);

router.route('/:id')
  .get(protect, getInvestmentById);

module.exports = router;
