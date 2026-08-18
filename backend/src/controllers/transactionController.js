const Transaction = require('../models/Transaction');

// @desc    Get all investment transactions
// @route   GET /api/transactions
exports.getTransactions = async (req, res, next) => {
  try {
    const { type, symbol } = req.query;
    const filter = { userId: req.user._id };

    if (type) filter.type = type;
    if (symbol) filter.symbol = symbol.toUpperCase();

    const transactions = await Transaction.find(filter).sort({ createdAt: -1 });

    res.status(200).json({
      success: true,
      count: transactions.length,
      data: transactions
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Create manual transaction
// @route   POST /api/transactions
exports.createTransaction = async (req, res, next) => {
  try {
    const { symbol, name, type, amount, qty, pricePerUnit, date } = req.body;

    const transaction = await Transaction.create({
      userId: req.user._id,
      symbol,
      name,
      type: type || 'BUY',
      amount,
      qty: qty || 0,
      pricePerUnit: pricePerUnit || 0,
      date: date || new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })
    });

    res.status(201).json({
      success: true,
      data: transaction
    });
  } catch (error) {
    next(error);
  }
};
