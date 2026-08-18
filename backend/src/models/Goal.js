const mongoose = require('mongoose');

const GoalSchema = new mongoose.Schema({
  userId: {
    type: mongoose.Schema.Types.ObjectId,
    ref: 'User',
    required: true,
    index: true
  },
  title: {
    type: String,
    required: [true, 'Please provide goal title'],
    trim: true
  },
  targetAmount: {
    type: Number,
    required: [true, 'Please provide target amount'],
    min: [100, 'Target amount must be at least ₹100']
  },
  currentAmount: {
    type: Number,
    default: 0,
    min: 0
  },
  targetDate: {
    type: String,
    required: true
  },
  category: {
    type: String,
    default: 'General'
  },
  colorHex: {
    type: String,
    default: '0xFF0284C7'
  },
  monthlySip: {
    type: Number,
    default: 0
  },
  priority: {
    type: String,
    enum: ['High', 'Medium', 'Low'],
    default: 'Medium'
  },
  status: {
    type: String,
    enum: ['IN_PROGRESS', 'COMPLETED', 'PAUSED'],
    default: 'IN_PROGRESS'
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

module.exports = mongoose.model('Goal', GoalSchema);
