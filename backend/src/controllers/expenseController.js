const Expense = require('../models/Expense');

// @desc    Get all expenses for user (with optional category or search filters)
// @route   GET /api/expenses
exports.getExpenses = async (req, res, next) => {
  try {
    const { category, search, startDate, endDate } = req.query;
    const filter = { userId: req.user._id };

    if (category && category !== 'All') {
      filter.category = category;
    }

    if (search) {
      filter.$or = [
        { title: { $regex: search, $options: 'i' } },
        { note: { $regex: search, $options: 'i' } },
        { merchantName: { $regex: search, $options: 'i' } }
      ];
    }

    const expenses = await Expense.find(filter).sort({ createdAt: -1 });

    const totalExpense = expenses.reduce((acc, curr) => acc + curr.amount, 0);

    // Group by category for visual analytics
    const categoryTotals = {};
    expenses.forEach((item) => {
      categoryTotals[item.category] = (categoryTotals[item.category] || 0) + item.amount;
    });

    const categoryBreakdown = Object.keys(categoryTotals).map((cat) => ({
      category: cat,
      amount: categoryTotals[cat],
      percentage: totalExpense > 0 ? ((categoryTotals[cat] / totalExpense) * 100).toFixed(1) : 0
    }));

    res.status(200).json({
      success: true,
      count: expenses.length,
      totalExpense,
      categoryBreakdown,
      data: expenses
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Get single expense by ID
// @route   GET /api/expenses/:id
exports.getExpenseById = async (req, res, next) => {
  try {
    const expense = await Expense.findOne({
      _id: req.params.id,
      userId: req.user._id
    });

    if (!expense) {
      return res.status(404).json({
        success: false,
        error: `Expense not found with ID of ${req.params.id}`
      });
    }

    res.status(200).json({
      success: true,
      data: expense
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Add a new expense
// @route   POST /api/expenses
exports.createExpense = async (req, res, next) => {
  try {
    const {
      title,
      category,
      amount,
      formattedDate,
      iconName,
      colorHex,
      paymentMode,
      merchantName,
      note
    } = req.body;

    if (!title || amount === undefined || amount === null) {
      return res.status(400).json({
        success: false,
        error: 'Please provide both title and amount for the expense'
      });
    }

    const expense = await Expense.create({
      userId: req.user._id,
      title: title.trim(),
      category: category || 'Other',
      amount: Number(amount),
      formattedDate:
        formattedDate ||
        new Date().toLocaleDateString('en-US', {
          month: 'short',
          day: 'numeric',
          year: 'numeric'
        }),
      iconName: iconName || 'receipt',
      colorHex: colorHex || '0xFF0284C7',
      paymentMode: paymentMode || 'UPI',
      merchantName: merchantName || '',
      note: note || ''
    });

    res.status(201).json({
      success: true,
      message: 'Expense recorded successfully',
      data: expense
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Update an existing expense
// @route   PUT /api/expenses/:id
exports.updateExpense = async (req, res, next) => {
  try {
    let expense = await Expense.findOne({
      _id: req.params.id,
      userId: req.user._id
    });

    if (!expense) {
      return res.status(404).json({
        success: false,
        error: `Expense not found with ID of ${req.params.id}`
      });
    }

    const fieldsToUpdate = [
      'title',
      'category',
      'amount',
      'formattedDate',
      'iconName',
      'colorHex',
      'paymentMode',
      'merchantName',
      'note'
    ];

    fieldsToUpdate.forEach((field) => {
      if (req.body[field] !== undefined) {
        expense[field] = req.body[field];
      }
    });

    await expense.save();

    res.status(200).json({
      success: true,
      message: 'Expense updated successfully',
      data: expense
    });
  } catch (error) {
    next(error);
  }
};

// @desc    Delete an expense
// @route   DELETE /api/expenses/:id
exports.deleteExpense = async (req, res, next) => {
  try {
    const expense = await Expense.findOne({
      _id: req.params.id,
      userId: req.user._id
    });

    if (!expense) {
      return res.status(404).json({
        success: false,
        error: `Expense not found with ID of ${req.params.id}`
      });
    }

    await expense.deleteOne();

    res.status(200).json({
      success: true,
      message: 'Expense deleted successfully',
      data: {}
    });
  } catch (error) {
    next(error);
  }
};
