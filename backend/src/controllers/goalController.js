const Goal = require('../models/Goal');

// @desc    Get all financial goals
// @route   GET /api/goals
exports.getGoals = async (req, res, next) => {
  try {
    const goals = await Goal.find({ userId: req.user._id }).sort({ createdAt: -1 });

    const totalTarget = goals.reduce((acc, curr) => acc + curr.targetAmount, 0);
    const totalSaved = goals.reduce((acc, curr) => acc + curr.currentAmount, 0);
    const totalMonthlySip = goals.reduce((acc, curr) => acc + curr.monthlySip, 0);

    res.status(200).json({
      success: true,
      summary: {
        totalGoals: goals.length,
        totalTarget,
        totalSaved,
        totalMonthlySip,
        overallProgress: totalTarget > 0 ? ((totalSaved / totalTarget) * 100).toFixed(1) : 0
      },
      data: goals
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Create new financial goal
// @route   POST /api/goals
exports.createGoal = async (req, res, next) => {
  try {
    const { title, targetAmount, currentAmount, targetDate, category, colorHex, monthlySip, priority } = req.body;

    const goal = await Goal.create({
      userId: req.user._id,
      title,
      targetAmount,
      currentAmount: currentAmount || 0,
      targetDate,
      category: category || 'General',
      colorHex: colorHex || '0xFF0284C7',
      monthlySip: monthlySip || 0,
      priority: priority || 'Medium'
    });

    res.status(201).json({
      success: true,
      data: goal
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Contribute amount to a goal
// @route   PATCH /api/goals/:id/contribute
exports.contributeToGoal = async (req, res, next) => {
  try {
    const { amount } = req.body;
    if (!amount || amount <= 0) {
      return res.status(400).json({ success: false, error: 'Please provide valid contribution amount' });
    }

    const goal = await Goal.findOne({ _id: req.params.id, userId: req.user._id });
    if (!goal) {
      return res.status(404).json({ success: false, error: 'Goal not found' });
    }

    goal.currentAmount += Number(amount);
    if (goal.currentAmount >= goal.targetAmount) {
      goal.status = 'COMPLETED';
    }
    await goal.save();

    res.status(200).json({
      success: true,
      data: goal
    });
  } catch (error) {
    next(error);
  }
};
