const jwt = require('jsonwebtoken');
const User = require('../models/User');

const protect = async (req, res, next) => {
  let token;

  if (
    req.headers.authorization &&
    req.headers.authorization.startsWith('Bearer')
  ) {
    token = req.headers.authorization.split(' ')[1];
  }

  // If no token provided, allow dev fallback user or reject
  if (!token) {
    // For quick testing and seamless dev, create/find default mock user if configured
    if (process.env.ALLOW_ANONYMOUS_DEV === 'true' || process.env.NODE_ENV === 'development') {
      let defaultUser = await User.findOne({ email: 'alex.riviera@spendwise.io' });
      if (!defaultUser) {
        defaultUser = await User.create({
          name: 'Alex Riviera',
          email: 'alex.riviera@spendwise.io',
          password: 'password123',
          phone: '+91 98765 43210',
          panNumber: 'ABCDE1234F',
          riskProfile: 'Moderate Growth'
        });
      }
      req.user = defaultUser;
      return next();
    }

    return res.status(401).json({
      success: false,
      error: 'Not authorized to access this route. Please provide a Bearer token.'
    });
  }

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET || 'spendwise_secret');
    req.user = await User.findById(decoded.id);
    if (!req.user) {
      return res.status(401).json({ success: false, error: 'User no longer exists' });
    }
    next();
  } catch (err) {
    return res.status(401).json({
      success: false,
      error: 'Invalid or expired authentication token'
    });
  }
};

module.exports = { protect };
