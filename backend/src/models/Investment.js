const mongoose = require('mongoose');

const InvestmentSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    index: true
  },
  symbol: {
    type: String,
    required: [true, 'Please provide symbol/code'],
    uppercase: true,
    trim: true
  },
  name: {
    type: String,
    required: [true, 'Please provide investment name'],
    trim: true
  },
  type: {
    type: String,
    required: [true, 'Please provide investment type'],
    enum: ['STOCK', 'MUTUAL_FUND', 'GOLD', 'FD', 'CRYPTO', 'GOVT_BOND'],
    default: 'STOCK'
  },
  investedAmount: {
    type: Number,
    required: true,
    min: 0
  },
  currentValue: {
    type: Number,
    required: true,
    min: 0
  },
  pnl: {
    type: Number,
    required: true,
    default: 0
  },
  pnlPercentage: {
    type: Number,
    required: true,
    default: 0
  },
  holdingQty: {
    type: Number,
    default: 0
  },
  avgPrice: {
    type: Number,
    default: 0
  },
  ltp: {
    type: Number,
    default: 0
  },
  iconUrl: {
    type: String,
    default: ''
  },
  notes: {
    type: String,
    default: ''
  },
  updatedAt: {
    type: Date,
    default: Date.now
  }
});

// Calculate PnL before saving
InvestmentSchema.pre('save', function (next) {
  if (this.investedAmount > 0) {
    this.pnl = this.currentValue - this.investedAmount;
    this.pnlPercentage = ((this.currentValue - this.investedAmount) / this.investedAmount) * 100;
  }
  this.updatedAt = Date.now();
  next();
});

module.exports = mongoose.model('Investment', InvestmentSchema);
