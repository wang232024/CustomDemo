package com.example.customutil.designpattern.observer;

public interface ISubject {
        public void add(Gambler gambler);
        public void delete(Gambler gambler);
        public void notice();
}
