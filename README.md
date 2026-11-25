# Chess Application

A desktop chess application developed in **Java**, featuring a graphical interface built with **Swing**, chess rule validation, user authentication, and game persistence using JSON.

The project focuses on object-oriented programming, modular architecture, and the implementation of design patterns.

## Features

- Interactive chessboard using Java Swing
- Player vs. player and player vs. computer gameplay
- Legal move validation and pawn promotion
- Check, checkmate, and stalemate detection
- Draw detection, including position repetition
- User authentication using JSON-based storage
- Game saving and loading using JSON
- Captured piece tracking and score calculation

## Game Structure

The application follows a modular, object-oriented architecture:

- **Game & Board:** Game state, turns, piece positions, and move validation.
- **Pieces:** Chess pieces and their movement rules.
- **Players:** Player management and computer opponent logic.
- **UIs:** Graphical interface developed with Java Swing.
- **Utils & Exceptions:** Utility classes and error handling.

## Design Patterns

The application incorporates several object-oriented design patterns:

- **Singleton:** Centralized management of shared instances.
- **Observer:** Communication between game components and interface updates.
- **Factory:** Creation of chess piece objects.
- **Visitor:** Separation of operations from the objects they operate on.

## Technologies

- **Java** - Core game logic and object-oriented architecture
- **Java Swing** - Desktop graphical interface
- **JSON** - User authentication data and game persistence
