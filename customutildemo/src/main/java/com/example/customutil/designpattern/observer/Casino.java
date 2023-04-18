package com.example.customutil.designpattern.observer;

import java.util.ArrayList;
import java.util.List;

public class Casino implements ISubject {
        public List<Gambler> casinolist = new ArrayList<Gambler>();
        private String string;
        
        // 添加和删除需要考虑多线程情况
        @Override
        public void add(Gambler gambler) {
                synchronized (this) {
                        // TODO Auto-generated method stub
                        if (null != gambler && !casinolist.contains(gambler))
                                casinolist.add(gambler);
                }
        }

        @Override
        public void delete(Gambler gambler) {
                synchronized (this) {
                        // TODO Auto-generated method stub
                        if (casinolist.contains(gambler))
                                casinolist.remove(gambler);
                }
        }

        @Override
        public void notice() {
                // TODO Auto-generated method stub
                for (Gambler gambler : casinolist) {
                        gambler.update(string);
                }
        }
        
        public void setString(String string) {
                this.string = string;
                notice();
        }
        
}
