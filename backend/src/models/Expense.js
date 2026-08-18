const mongoose = require('mongoose');

const ExpenseSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    index: true
  },
  title: {
    type: String,
    required: [true, 'Please provide an expense title'],
    trim: true
  },
  category: {
    type: String,
    required: [true, 'Please provide an expense category'],
    enum: [
      'Dining & Food',
      'Shopping',
      'Transport & Fuel',
      'Entertainment',
      'Bills & Utilities',
      'Healthcare',
      'Housing & Rent',
      'Education',
      'Investment',
      'Other'
    ],
    default: 'Other'
  },
  amount: {
    type: Number,
    required: [true, 'Please provide an expense amount'],
    min: [0.01, 'Amount must be greater than 0']
  },
  formattedDate: {
    type: String,
    required: true
  },
  iconName: {
    type: String,
    default: 'receipt'
  },
  colorHex: {
    type: String,
    default: '0xFF0284C7'
  },
  paymentMode: {
    type: String,
    enum: ['UPI', 'Credit Card', 'Debit Card', 'Net Banking', 'Cash', 'Auto Debit'],
    default: 'UPI'
  },
  merchantName: {
    type: String,
    default: ''
  },
  note: {
    type: String,
    default: ''
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Expense', ExpenseSchema);
