#language: ru
@Test

Функционал: Получение информации по мультику Рик и Морти
  Предыстория: выбираем любого персонажа для получения информации
    Когда выбрали персонажа
      | 2 |
  Сценарий: сравниваем расы двух персонажей
    Тогда получили персонажа из последнего эпизода
    Тогда получили информацию по персонажу из последнего эпизода
    Тогда сравнили расы первого и второго персонажа

  Сценарий: сравниваем локации двух персонажей
    Тогда получили персонажа из последнего эпизода
    Тогда получили информацию по персонажу из последнего эпизода
    Тогда сравнили локации персонажей
