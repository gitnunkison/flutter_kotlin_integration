import 'base_state.dart';

class ErrorState extends BaseState {
  ErrorState({
    required message,
  }) : super(message: message);
}