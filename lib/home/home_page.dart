import 'package:flutter/material.dart';
import 'package:flutter_kotlin_interaction/base/error_screen.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';
import '../base/base_state.dart';
import '../base/error_state.dart';
import 'controller/home_controller_provider.dart';
import 'controller/success_home_state.dart';
import 'home_screen.dart';

class HomePage extends ConsumerWidget {
  final String title;

  const HomePage({
    Key? key,
    required this.title,
  }) : super(key: key);

  @override
  Widget build(BuildContext context, WidgetRef ref) {
    final state = ref.watch(homeControllerProvider);

    return Scaffold(
      appBar: AppBar(
        title: Text(title),
      ),
      body: _buildScreen(state),
      floatingActionButton: Visibility(child: FloatingActionButton(
        onPressed: () {
          final homeController = ref.read(homeControllerProvider.notifier);
          if (!homeController.isLoading()){
            homeController.loadLocation();
          }
        },
        tooltip: 'Get Location',
        child: const Icon(Icons.gps_fixed),
      )),
    );
  }

  Widget _buildScreen(BaseState baseState) {
    switch (baseState.runtimeType) {
      case SuccessHomeState:
        final state = baseState as SuccessHomeState;
        return HomeScreen(
          latLng: state.latLng,
          loading: state.loading,
          message: state.message,
        );
      case ErrorState:
        final state = baseState as ErrorState;
        return ErrorScreen(
          message: state.message ?? "Erro",
        );
      default:
        throw Exception("State not supported.");
    }
  }
}
