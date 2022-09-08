import 'package:flutter_kotlin_interaction/base/platform_interactor/patform_interactor_impl.dart';
import 'package:flutter_kotlin_interaction/base/platform_interactor/platform_interactor.dart';
import 'package:hooks_riverpod/hooks_riverpod.dart';

final platformInteractionProvider = Provider<PlatformInteractor>(
  (ref) {
    return PlatformInteractorImpl();
  },
);
