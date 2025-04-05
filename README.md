# Smart-contact-Manager
# Smart Contact Manager

Smart Contact Manager is a web-based application designed to help users efficiently manage their contacts. The application provides functionalities such as user authentication, adding, updating, and deleting contacts, and a responsive UI for a seamless experience(Working on Payment Gateway ,trying to add razorpay gateway for donation).



## 🚀 Features
- 🔐 **User Authentication** – Secure login and registration using Spring Security.
- 📞 **Manage Contacts** – Add, edit, delete, and view contacts.
- 🎨 **Responsive UI** – Optimized for both desktop and mobile users.


---

## 🛠️ Technologies Used
- **Backend:** Java, Spring Boot, Spring Security, Hibernate, JPA
- **Frontend:** Thymeleaf(Conditional Rendering), Bootstrap, HTML, CSS
- **Database:** MySQL
- **Build Tool:** Maven
- **Authentication:**  Spring Security with BCrypt Password Encoder


---

## 🎯 Project Structure

smartcontactmanager/   # Root project directory
│-- src/main/java/com/smart/  # Main application package
│   │-- config/                # Configuration files
│   │   ├── CustomUserDetails.java
│   │   ├── MyConfig.java
│   │   ├── UserDetailsServiceImpl.java
│   │
│   │-- controller/            # Controllers (Handles HTTP requests)
│   │   ├── HomeController.java
│   │   ├── UserController.java
│   │
│   │-- dao/                   # Data Access Layer (Repositories)
│   │   ├── ContactRepository.java
│   │   ├── UserRepository.java
│   │
│   │-- entities/              # Entity classes (Database Models)
│   │   ├── Contact.java
│   │   ├── User.java
│   │
│   │-- helper/                # Utility/Helper classes
│   │   ├── Message.java
│
│-- src/main/resources/        # Static resources & templates
│   │-- static/                 # Static assets (CSS, JS, Images)
│   │   ├── img/
│   │   ├── style.css
│   │
│   │-- templates/              # Thymeleaf Templates (HTML Views)
│   │   ├── normal/
│   │   │   ├── add_contact_form.html
│   │   │   ├── base.html
│   │   │   ├── contact_detail.html
│   │   │   ├── profile.html
│   │   │   ├── show_contact.html
│   │   │   ├── update_form.html
│   │   │   ├── user_dashboard.html
│   │   │
│   │   ├── base.html
│   │   ├── home.html
│   │   ├── login.html
│   │   ├── signup.html
│
│-- src/main/resources/application.properties  # Spring Boot Configurations
│
│-- src/test/java/              # Test Cases (if applicable)
│
│-- pom.xml                      # Maven Build File (Dependencies)
│-- README.md                     # Project Documentation
│-- .gitignore                    # Ignore unnecessary files in Git

