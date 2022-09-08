import 'package:flutter/cupertino.dart';

class ErrorScreen extends StatelessWidget {
  final String message;

  const ErrorScreen({
    Key? key,
    required this.message,
  }) : super(key: key);

  @override
  Widget build(BuildContext context) {
    return Center(
      child: Text(
        message,
      ),
    );
  }
}
