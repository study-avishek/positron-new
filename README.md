# Positron Project

![Positron Logo](https://pbs.twimg.com/profile_images/1547835647678763009/qv8i9vDR_400x400.jpg)

Positron is an innovative project that provides a comprehensive Point of Sale (PoS) system with two main modules: Pos App and Invoice App. It is built using a powerful tech stack, including HTML5, Bootstrap 4, Spring Framework, Hibernate, Apache FOP, JPA, MySQL, Thymeleaf, and JavaScript. This versatile system is designed to streamline and enhance various retail operations, making it a perfect choice for businesses looking to modernize their point of sale experience.

## Features

### Brand Master

- Upload brand/category details using TSV file from the user interface.
- View, create, and edit brand details through the user interface.
- Ensure that a product cannot be inserted if its brand-category does not exist in the Brand Master.
- Maintain a centralized repository for all brand and category details.

### Product Master

- Upload product details using TSV file from the user interface.
- View, create, and edit product details through the user interface.
- Ensure that a product cannot be created without an existing brand-category combination.
- Manage product inventory efficiently.

### Inventory Management

- Upload product-wise inventory details using TSV file from the user interface.
- Edit inventory for a product, ensuring real-time stock management.
- Track product inventory changes during customer orders to avoid overselling.

### Customer Order

- Create and manage customer orders through a user-friendly interface.
- Enter barcode, quantity, and MRP for multiple products in one order.
- Ensure that an order cannot be created for a product that does not exist in the system.
- Reduce inventory upon order creation to maintain accurate stock levels.

### Invoice Generation Module

- Generate PDF invoices for customer orders.
- Utilize the separate Invoice App module, present in the same repository as Pos App.
- The Invoice App returns a Base64 encoded string to PoS, which converts it into a PDF, stores it locally, and saves the path in the Invoice entity.

### Reports

- Generate inventory reports to get insights into product availability.
- View sales reports for a specified duration to analyze business performance.
- Daily sales scheduler which updates every minute to showcase day-by-day sale.
- Brand report to see brand-category availability

### Authentication and Role Assignment

- Basic signup page that collects the user's email.
- Based on the email, assign roles from the properties file (operator/supervisor).
- Operators cannot upload masters, manage inventory, or create orders.
- Supervisors have access to all API endpoints and functionalities.

## Getting Started

To set up and run the Positron project, follow these steps:

1. Clone the repository from GitHub: `git clone https://github.com/your-username/positron.git`
2. Install the required dependencies using [Maven](https://maven.apache.org/).
3. Configure the database settings in the application properties.
4. Run the Pos App and Invoice App modules independently(Run invoice-app first).

## Support

If you have any questions, feedback, or encounter issues, please [open an issue](https://github.com/your-username/positron/issues) on GitHub. We appreciate your valuable input!

Happy retailing with Positron! 🚀