const Investment = require('../models/Investment');
const Transaction = require('../models/Transaction');

// @desc    Get all investments and breakdown
// @route   GET /api/investments
exports.getInvestments = async (req, res, next) => {
  try {
    const investments = await Investment.find({ userId: req.user._id });

    const totalInvested = investments.reduce((acc, curr) => acc + curr.investedAmount, 0);
    const totalCurrentValue = investments.reduce((acc, curr) => acc + curr.currentValue, 0);
    const totalReturns = totalCurrentValue - totalInvested;
    const overallReturnPercentage = totalInvested > 0 ? ((totalReturns / totalInvested) * 100).toFixed(2) : 0;

    // Asset allocation breakdown
    const assetMap = {};
    investments.forEach((item) => {
      assetMap[item.type] = (assetMap[item.type] || 0) + item.currentValue;
    });

    const assetAllocation = Object.keys(assetMap).map((type) => ({
      type,
      value: assetMap[type],
      percentage: totalCurrentValue > 0 ? ((assetMap[type] / totalCurrentValue) * 100).toFixed(1) : 0
    }));

    res.status(200).json({
      success: true,
      summary: {
        totalInvested,
        totalCurrentValue,
        totalReturns,
        overallReturnPercentage: parseFloat(overallReturnPercentage),
        totalAssetsCount: investments.length
      },
      assetAllocation,
      data: investments
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Add new investment holding
// @route   POST /api/investments
exports.createInvestment = async (req, res, next) => {
  try {
    const { symbol, name, type, investedAmount, currentValue, holdingQty, avgPrice, ltp } = req.body;

    const investment = await Investment.create({
      userId: req.user._id,
      symbol,
      name,
      type: type || 'STOCK',
      investedAmount,
      currentValue: currentValue || investedAmount,
      holdingQty: holdingQty || 0,
      avgPrice: avgPrice || 0,
      ltp: ltp || avgPrice || 0
    });

    // Automatically record corresponding BUY transaction
    await Transaction.create({
      userId: req.user._id,
      investmentId: investment._id,
      symbol: investment.symbol,
      name: investment.name,
      type: 'BUY',
      amount: investment.investedAmount,
      qty: investment.holdingQty,
      pricePerUnit: investment.avgPrice,
      date: new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
    });

    res.status(201).json({
      success: true,
      data: investment
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Get investment detail by id
// @route   GET /api/investments/:id
exports.getInvestmentById = async (req, res, next) => {
  try {
    const investment = await Investment.findOne({ _id: req.params.id, userId: req.user._id });
    if (!investment) {
      return res.status(404).json({ success: false, error: 'Investment asset not found' });
    }

    const transactions = await Transaction.find({ investmentId: investment._id }).sort({ createdAt: -1 });

    res.status(200).json({
      success: true,
      data: investment,
      transactions
    });
  } catch (error) {
    next(error);
  }
};
