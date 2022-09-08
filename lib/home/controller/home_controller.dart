import 'package:hooks_riverpod/hooks_riverpod.dart';

import '../../base/base_state.dart';

abstract class HomeController<T> extends StateNotifier<BaseState> {
  HomeController(super.state);

  Future<void> loadLocation();
  bool isLoading();
}