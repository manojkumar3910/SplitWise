const express = require('express');
const { getGoals, createGoal, contributeToGoal } = require('../controllers/goalController');
const { protect } = require('../middleware/auth');

const router = express.Router();

router.route('/')
  .get(protect, getGoals)
  .post(protect, createGoal);

router.route('/:id/contribute')
  .patch(protect, contributeToGoal);

module.exports = router;
