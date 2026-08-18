const Expense = require('../models/Expense');
const Investment = require('../models/Investment');
const Goal = require('../models/Goal');

// @desc    Get dashboard summary cards and analytics
// @route   GET /api/dashboard/summary
exports.getDashboardSummary = async (req, res, next) => {
  try {
    const userId = req.user._id;

    // 1. Fetch investments
    const investments = await Investment.find({ userId });
    const totalPortfolioValue = investments.reduce((acc, curr) => acc + curr.currentValue, 0);
    const totalInvested = investments.reduce((acc, curr) => acc + curr.investedAmount, 0);
    const totalPnl = totalPortfolioValue - totalInvested;
    const overallPnlPercentage = totalInvested > 0 ? ((totalPnl / totalInvested) * 100).toFixed(2) : 0;

    // 2. Fetch monthly expenses
    const expenses = await Expense.find({ userId });
    const totalMonthlyExpense = expenses.reduce((acc, curr) => acc + curr.amount, 0);

    // 3. User income and net worth
    const monthlyIncome = req.user.monthlyIncome || 95000.0;
    const netSavings = monthlyIncome - totalMonthlyExpense;
    const savingsRate = monthlyIncome > 0 ? ((netSavings / monthlyIncome) * 100).toFixed(1) : 0;

    // 4. Fetch Goals
    const goals = await Goal.find({ userId });
    const activeGoalsCount = goals.length;
    const totalGoalTarget = goals.reduce((acc, curr) => acc + curr.targetAmount, 0);
    const totalGoalSaved = goals.reduce((acc, curr) => acc + curr.currentAmount, 0);

    res.status(200).json({
      success: true,
      data: {
        totalNetWorth: totalPortfolioValue + netSavings + 50000, // including bank balance
        monthlyIncome,
        monthlyExpense: totalMonthlyExpense,
        netSavings,
        savingsRate: parseFloat(savingsRate),
        portfolio: {
          totalValue: totalPortfolioValue,
          invested: totalInvested,
          pnl: totalPnl,
          pnlPercentage: parseFloat(overallPnlPercentage),
          holdingsCount: investments.length
        },
        goals: {
          activeCount: activeGoalsCount,
          totalTarget: totalGoalTarget,
          totalSaved: totalGoalSaved,
          progressPercentage: totalGoalTarget > 0 ? ((totalGoalSaved / totalGoalTarget) * 100).toFixed(1) : 0
        },
        financialHealthScore: 84
      }
    });
  } catch (error) {
    next(error);
  }
};
