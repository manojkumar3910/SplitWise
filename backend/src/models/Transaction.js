const mongoose = require('mongoose');

const TransactionSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    index: true
  },
  investmentId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'Investment'
  },
  symbol: {
    type: String,
    required: true,
    uppercase: true
  },
  name: {
    type: String,
    required: true
  },
  type: {
    type: String,
    enum: ['BUY', 'SELL', 'SIP_AUTO_DEBIT', 'DIVIDEND', 'INTEREST'],
    required: true
  },
  amount: {
    type: Number,
    required: true
  },
  qty: {
    type: Number,
    default: 0
  },
  pricePerUnit: {
    type: Number,
    default: 0
  },
  date: {
    type: String,
    required: true
  },
  status: {
    type: String,
    enum: ['SUCCESS', 'PROCESSING', 'FAILED'],
    default: 'SUCCESS'
  },
  referenceId: {
    type: String,
    default: () => 'TXN' + Math.random().toString(36).substring(2, 9).toUpperCase()
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Transaction', TransactionSchema);
