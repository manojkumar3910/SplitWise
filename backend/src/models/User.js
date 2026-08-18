const mongoose = require('mongoose');
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');

const UserSchema = new mongoose.Schema({
  name: {
    type: String,
    required: [true, 'Please add a name'],
    trim: true,
    default: 'Alex Riviera'
  },
  email: {
    type: String,
    required: [true, 'Please add an email'],
    unique: true,
    lowercase: true,
    trim: true,
    match: [
      /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/,
      'Please add a valid email'
    ]
  },
  password: {
    type: String,
    required: [true, 'Please add a password'],
    minlength: 6,
    select: false
  },
  phone: {
    type: String,
    default: '+91 98765 43210'
  },
  panNumber: {
    type: String,
    default: 'ABCDE1234F'
  },
  riskProfile: {
    type: String,
    enum: ['Conservative', 'Moderate Growth', 'Aggressive Growth', 'High Risk'],
    default: 'Moderate Growth'
  },
  kycStatus: {
    type: String,
    enum: ['VERIFIED', 'PENDING', 'UNVERIFIED'],
    default: 'VERIFIED'
  },
  tier: {
    type: String,
    default: 'Pro Wealth'
  },
  monthlyIncome: {
    type: Number,
    default: 95000.0
  },
  emergencyFundGoal: {
    type: Number,
    default: 150000.0
  },
  createdAt: {
    type: Date,
    default: Date.now
  }
});

// Encrypt password using bcrypt before save
UserSchema.pre('save', async function (next) {
  if (!this.isModified('password')) {
    return next();
  }
  const salt = await bcrypt.genSalt(10);
  this.password = await bcrypt.hash(this.password, salt);
  next();
});

// Sign JWT and return
UserSchema.methods.getSignedJwtToken = function () {
  return jwt.sign(
    { id: this._id, email: this.email, name: this.name },
    process.env.JWT_SECRET || 'spendwise_secret',
    { expiresIn: process.env.JWT_EXPIRE || '30d' }
  );
};

// Match user entered password to hashed password in database
UserSchema.methods.matchPassword = async function (enteredPassword) {
  return await bcrypt.compare(enteredPassword, this.password);
};

module.exports = mongoose.model('User', UserSchema);
