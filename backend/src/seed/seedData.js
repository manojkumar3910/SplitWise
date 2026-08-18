const mongoose = require('mongoose');
const dotenv = require('dotenv');

dotenv.config({ path: './.env' });

const User = require('../models/User');
const Expense = require('../models/Expense');
const Investment = require('../models/Investment');
const Goal = require('../models/Goal');
const Transaction = require('../models/Transaction');

const seedDatabase = async () => {
  try {
    const mongoUri = process.env.MONGODB_URI || 'mongodb://localhost:27017/spendwise';
    await mongoose.connect(mongoUri);
    console.log(`[Seed]: Connected to MongoDB at ${mongoUri}`);

    // Clear existing collections
    await User.deleteMany({});
    await Expense.deleteMany({});
    await Investment.deleteMany({});
    await Goal.deleteMany({});
    await Transaction.deleteMany({});

    console.log('[Seed]: Cleared old data.');

    // 1. Create Default User (Alex Riviera)
    const user = await User.create({
      name: 'Alex Riviera',
      email: 'alex.riviera@spendwise.io',
      password: 'password123',
      phone: '+91 98765 43210',
      panNumber: 'ABCDE1234F',
      riskProfile: 'Moderate Growth',
      kycStatus: 'VERIFIED',
      tier: 'Pro Wealth',
      monthlyIncome: 95000.0
    });

    console.log(`[Seed]: Created User -> ${user.email}`);

    // 2. Create Sample Expenses
    const sampleExpenses = [
      {
        userId: user._id,
        title: 'Bistro Gourmet & Drinks',
        category: 'Dining & Food',
        amount: 3450.0,
        formattedDate: 'Today, 2:30 PM',
        iconName: 'restaurant',
        colorHex: '0xFF0284C7',
        paymentMode: 'Credit Card',
        note: 'Weekend family lunch'
      },
      {
        userId: user._id,
        title: 'Highland Supermarket',
        category: 'Shopping',
        amount: 8210.0,
        formattedDate: 'Yesterday, 6:15 PM',
        iconName: 'shopping_bag',
        colorHex: '0xFF0EA5E9',
        paymentMode: 'UPI',
        note: 'Monthly groceries and household items'
      },
      {
        userId: user._id,
        title: 'Shell Fuel Station',
        category: 'Transport & Fuel',
        amount: 4500.0,
        formattedDate: 'Aug 14, 9:00 AM',
        iconName: 'local_gas_station',
        colorHex: '0xFF38BDF8',
        paymentMode: 'Debit Card',
        note: 'Full tank premium petrol'
      },
      {
        userId: user._id,
        title: 'Fiber Internet & OTT Bundle',
        category: 'Bills & Utilities',
        amount: 2199.0,
        formattedDate: 'Aug 10, 11:20 AM',
        iconName: 'wifi',
        colorHex: '0xFF0284C7',
        paymentMode: 'Auto Debit',
        note: 'High-speed broadband annual plan'
      },
      {
        userId: user._id,
        title: 'Apple One Subscription',
        category: 'Entertainment',
        amount: 365.0,
        formattedDate: 'Aug 05, 4:00 PM',
        iconName: 'subscriptions',
        colorHex: '0xFF7DD3FC',
        paymentMode: 'Credit Card',
        note: 'Cloud and music streaming'
      }
    ];

    await Expense.insertMany(sampleExpenses);
    console.log(`[Seed]: Created ${sampleExpenses.length} expenses.`);

    // 3. Create Sample Investments
    const sampleInvestments = [
      {
        userId: user._id,
        symbol: 'RELIANCE',
        name: 'Reliance Industries Ltd.',
        type: 'STOCK',
        investedAmount: 52000.0,
        currentValue: 64800.0,
        pnl: 12800.0,
        pnlPercentage: 24.6,
        holdingQty: 22,
        avgPrice: 2363.63,
        ltp: 2945.45
      },
      {
        userId: user._id,
        symbol: 'TCS',
        name: 'Tata Consultancy Services',
        type: 'STOCK',
        investedAmount: 48000.0,
        currentValue: 56400.0,
        pnl: 8400.0,
        pnlPercentage: 17.5,
        holdingQty: 14,
        avgPrice: 3428.57,
        ltp: 4028.57
      },
      {
        userId: user._id,
        symbol: 'NIFTY50',
        name: 'Mirae Asset Nifty 50 Index Fund',
        type: 'MUTUAL_FUND',
        investedAmount: 75000.0,
        currentValue: 92300.0,
        pnl: 17300.0,
        pnlPercentage: 23.06,
        holdingQty: 450,
        avgPrice: 166.67,
        ltp: 205.11
      },
      {
        userId: user._id,
        symbol: 'SGB2028',
        name: 'Sovereign Gold Bond Series VI',
        type: 'GOLD',
        investedAmount: 35000.0,
        currentValue: 44200.0,
        pnl: 9200.0,
        pnlPercentage: 26.28,
        holdingQty: 7,
        avgPrice: 5000.0,
        ltp: 6314.28
      }
    ];

    const createdInvestments = await Investment.insertMany(sampleInvestments);
    console.log(`[Seed]: Created ${createdInvestments.length} investment holdings.`);

    // 4. Create Transactions
    const sampleTransactions = [
      {
        userId: user._id,
        investmentId: createdInvestments[0]._id,
        symbol: 'RELIANCE',
        name: 'Reliance Industries Ltd.',
        type: 'BUY',
        amount: 23630.0,
        qty: 10,
        pricePerUnit: 2363.0,
        date: 'Aug 12, 2025',
        status: 'SUCCESS'
      },
      {
        userId: user._id,
        investmentId: createdInvestments[2]._id,
        symbol: 'NIFTY50',
        name: 'Mirae Asset Nifty 50 Fund',
        type: 'SIP_AUTO_DEBIT',
        amount: 15000.0,
        qty: 75,
        pricePerUnit: 200.0,
        date: 'Aug 01, 2025',
        status: 'SUCCESS'
      }
    ];
    await Transaction.insertMany(sampleTransactions);
    console.log(`[Seed]: Created ${sampleTransactions.length} transactions.`);

    // 5. Create Goals
    const sampleGoals = [
      {
        userId: user._id,
        title: 'Emergency Rainy Day Fund',
        targetAmount: 150000.0,
        currentAmount: 95000.0,
        targetDate: 'Dec 2025',
        category: 'Emergency',
        colorHex: '0xFF0284C7',
        monthlySip: 10000.0,
        priority: 'High'
      },
      {
        userId: user._id,
        title: 'Tokyo Vacation 2026',
        targetAmount: 250000.0,
        currentAmount: 112000.0,
        targetDate: 'May 2026',
        category: 'Travel',
        colorHex: '0xFF0EA5E9',
        monthlySip: 15000.0,
        priority: 'Medium'
      },
      {
        userId: user._id,
        title: 'Electric Vehicle Down Payment',
        targetAmount: 400000.0,
        currentAmount: 180000.0,
        targetDate: 'Oct 2026',
        category: 'Automobile',
        colorHex: '0xFF0284C7',
        monthlySip: 20000.0,
        priority: 'High'
      }
    ];

    await Goal.insertMany(sampleGoals);
    console.log(`[Seed]: Created ${sampleGoals.length} goals.`);

    console.log('\n✅ [Seed]: Database successfully populated with production demo data!');
    process.exit(0);
  } catch (error) {
    console.error('[Seed Error]:', error);
    process.exit(1);
  }
};

seedDatabase();
