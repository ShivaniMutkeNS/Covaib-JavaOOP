# Online Exam System with Anti-Cheating

## Problem Statement
Encapsulate student answers[] so they can only be set during exam and read only by ExamEvaluator. Prevent student from seeing their answers once submitted.

## Solution Overview

### Key Features
1. **Answer Encapsulation**: Student answers are private and protected
2. **Time-Based Access**: Answers can only be set during active exam
3. **Role-Based Reading**: Only ExamEvaluator can read answers
4. **Anti-Cheating Measures**: Comprehensive logging and validation
5. **Submission Control**: One-time submission with validation

### Encapsulation Principles Demonstrated

#### 1. Data Hiding
- Student answers are private and cannot be accessed directly
- No way for students to see their answers after submission
- All access through controlled methods

#### 2. Access Control
- Answers can only be set during active exam
- Only authorized evaluators can read answers
- Student ID validation for all operations

#### 3. Anti-Cheating Protection
- Complete logging of all activities
- Validation of all operations
- Prevention of unauthorized access

#### 4. Time Management
- Exam sessions with time limits
- Automatic expiration handling
- Time-based access control

## Class Structure

### StudentAnswer Class
```java
public class StudentAnswer {
    // Encapsulated data
    private final String studentId;
    private final String examId;
    private final Map<Integer, String> answers;
    private final LocalDateTime submissionTime;
    private final boolean isSubmitted;
    private final List<AntiCheatingEvent> antiCheatingEvents;
    private final ExamSession session;
    
    // Public methods with access control
    public boolean setAnswer(int questionNumber, String answer, String studentId)
    public boolean submitExam(String studentId)
    public String getAnswer(int questionNumber, ExamEvaluator evaluator)
    public Map<Integer, String> getAllAnswers(ExamEvaluator evaluator)
    public List<AntiCheatingEvent> getAntiCheatingEvents(ExamEvaluator evaluator)
}
```

## Access Control Levels

### Student Access
- **Can Do**: Set answers during active exam
- **Cannot Do**: Read answers after submission
- **Validation**: Student ID must match

### Evaluator Access
- **Can Do**: Read all answers and anti-cheating events
- **Cannot Do**: Modify answers or exam state
- **Validation**: Must be authorized evaluator

### System Access
- **Can Do**: Log all activities and validate operations
- **Cannot Do**: Access answers without proper authorization
- **Validation**: All operations are logged and validated

## Usage Examples

### Creating Exam Session
```java
// Create exam session (60 minutes)
StudentAnswer.ExamSession session = new StudentAnswer.ExamSession("EXAM_001", 60);

// Create student answer sheet
StudentAnswer answerSheet = new StudentAnswer("STU_001", "EXAM_001", session);
```

### Setting Answers (Student)
```java
// Set answers during exam
boolean success1 = answerSheet.setAnswer(1, "Option A", "STU_001");
boolean success2 = answerSheet.setAnswer(2, "Option B", "STU_001");
boolean success3 = answerSheet.setAnswer(3, "Option C", "STU_001");

// Submit exam
boolean submitted = answerSheet.submitExam("STU_001");
```

### Reading Answers (Evaluator)
```java
// Create authorized evaluator
StudentAnswer.ExamEvaluator evaluator = new StudentAnswer.ExamEvaluator("EVAL_001", "EVAL_AUTH_2024");

// Read individual answers
String answer1 = answerSheet.getAnswer(1, evaluator);
String answer2 = answerSheet.getAnswer(2, evaluator);

// Read all answers
Map<Integer, String> allAnswers = answerSheet.getAllAnswers(evaluator);

// Read anti-cheating events
List<AntiCheatingEvent> events = answerSheet.getAntiCheatingEvents(evaluator);
```

## Anti-Cheating Measures

### 1. Answer Protection
- **Private Storage**: Answers are stored in private fields
- **No Direct Access**: No getter methods for students
- **Encapsulation**: Complete data hiding

### 2. Time-Based Control
- **Exam Sessions**: Time-limited exam periods
- **Automatic Expiration**: Answers cannot be set after exam ends
- **Time Validation**: All operations check exam status

### 3. Access Validation
- **Student ID Validation**: All operations validate student identity
- **Evaluator Authorization**: Only authorized evaluators can read
- **Role-Based Access**: Different access levels for different roles

### 4. Activity Logging
- **Complete Audit Trail**: All activities are logged
- **Anti-Cheating Events**: Suspicious activities are flagged
- **Non-Repudiation**: Cannot deny activities

## Security Features

### 1. Data Encapsulation
- **Private Fields**: All sensitive data is private
- **Controlled Access**: Only through authorized methods
- **No Bypass**: No way to access data directly

### 2. Access Control
- **Role-Based**: Different permissions for different roles
- **Time-Based**: Access only during valid periods
- **Validation-Based**: All operations are validated

### 3. Anti-Cheating Protection
- **Activity Monitoring**: All activities are monitored
- **Suspicious Activity Detection**: Unusual patterns are flagged
- **Audit Trail**: Complete record of all activities

### 4. Input Validation
- **Data Validation**: All inputs are validated
- **Format Checking**: Data format is verified
- **Range Checking**: Values are within expected ranges

## Anti-Cheating Events

### Event Types
- **ANSWER_SHEET_CREATED**: Answer sheet creation
- **ANSWER_SET**: Answer setting
- **EXAM_SUBMITTED**: Exam submission
- **UNAUTHORIZED_ACCESS**: Unauthorized access attempts
- **EXAM_ENDED**: Exam expiration
- **INVALID_ANSWER**: Invalid answer attempts
- **ALREADY_SUBMITTED**: Double submission attempts

### Event Logging
- **Timestamp**: When event occurred
- **Event Type**: Type of event
- **Description**: Detailed description
- **Student ID**: Who triggered the event

## Time Management

### Exam Sessions
- **Start Time**: When exam begins
- **End Time**: When exam ends
- **Duration**: Total exam time
- **Remaining Time**: Time left in exam

### Time Validation
- **Active Check**: All operations check if exam is active
- **Expiration Handling**: Automatic handling of expired exams
- **Time Remaining**: Real-time calculation of remaining time

## Error Handling

### Validation Errors
- **Invalid Student ID**: Wrong student attempting operation
- **Invalid Answer**: Empty or null answers
- **Exam Expired**: Operations after exam ends
- **Already Submitted**: Double submission attempts

### Security Errors
- **Unauthorized Access**: Access without proper authorization
- **Invalid Evaluator**: Unauthorized evaluator attempts
- **Access Denied**: Insufficient permissions

## Performance Considerations

### Memory Usage
- **Answer Storage**: Efficient storage of answers
- **Event Logging**: Consider log rotation for large systems
- **Session Management**: Efficient session handling

### Time Complexity
- **Answer Setting**: O(1) for individual answers
- **Answer Retrieval**: O(1) for individual answers
- **Event Logging**: O(1) for event addition

## Best Practices

1. **Use Time-Based Sessions** for exam management
2. **Implement Complete Logging** for anti-cheating
3. **Validate All Inputs** before processing
4. **Use Role-Based Access Control** for security
5. **Handle Edge Cases** properly
6. **Test Security Thoroughly** with different scenarios
7. **Document Access Patterns** clearly

## Anti-Patterns Avoided

- ❌ Exposing answers directly to students
- ❌ Allowing access after exam ends
- ❌ Not validating student identity
- ❌ Not logging suspicious activities
- ❌ Allowing unauthorized evaluator access
- ❌ Not handling time expiration
- ❌ Not validating input data
- ❌ Exposing internal implementation details

## Compliance and Standards

### Educational Standards
- **Academic Integrity**: Prevents cheating and plagiarism
- **Fair Assessment**: Ensures equal opportunity for all students
- **Transparency**: Clear rules and procedures

### Security Standards
- **Data Protection**: Sensitive data is protected
- **Access Control**: Proper authorization required
- **Audit Trail**: Complete activity logging

### Technical Standards
- **Encapsulation**: Proper object-oriented design
- **Validation**: Input validation and error handling
- **Performance**: Efficient data structures and algorithms
